package com.pacman.hospital.domain.payment.service.impl;

import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.billing.model.BillingStatus;
import com.pacman.hospital.domain.billing.repository.BillingRepository;
import com.pacman.hospital.domain.invoice.model.Invoice;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;
import com.pacman.hospital.domain.invoice.repository.InvoiceRepository;
import com.pacman.hospital.domain.payment.dto.PaymentDto;
import com.pacman.hospital.domain.payment.mapper.PaymentMapper;
import com.pacman.hospital.domain.payment.model.Payment;
import com.pacman.hospital.domain.payment.repository.PaymentRepository;
import com.pacman.hospital.domain.payment.service.PaymentService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final BillingRepository billingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository,
            BillingRepository billingRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.billingRepository = billingRepository;
    }

    @Override
    public List<PaymentDto> createPayment(PaymentDto paymentDto) {
        // find invoice by id
        Invoice existingInvoice = invoiceRepository.findById(paymentDto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found with id: " + paymentDto.getInvoiceId() + ""));
        // find billing by id
        Billing existingBill = null;
        if (paymentDto.getBillingId() != null) {
            existingBill = billingRepository.findById(paymentDto.getBillingId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Billing not found with id: " + paymentDto.getBillingId() + ""));
        }

        // build payment
        Payment payment = PaymentMapper.toEntity(paymentDto);
        payment.setInvoice(existingInvoice);
        payment.setBilling(existingBill);
        if (payment.getPaidAt() != null) { // checking paid or not
            payment.setPaidAt(LocalDateTime.now());
        }
        // save payment
        Payment savedPayment = paymentRepository.save(payment);
        // reconcile invoice: subtract payment amount from amountDue (BigDecimal
        // arithmetic)
        BigDecimal amount = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
        BigDecimal currentDue = existingInvoice.getAmountDue() != null ? existingInvoice.getAmountDue()
                : BigDecimal.ZERO;
        BigDecimal newDue = currentDue.subtract(amount); // newDue =amount -currentDue
        // check if newDue is negative then set it to zero
        if (newDue.compareTo(BigDecimal.ZERO) <= 0) {
            existingInvoice.setAmountDue(BigDecimal.ZERO); // setting to zero that means paid
            existingInvoice.setStatus(InvoiceStatus.PAID);
        } else {
            existingInvoice.setAmountDue(newDue);
            existingInvoice.setStatus(InvoiceStatus.PENDING);
        }
        //// updating billing info if linked
        if (existingBill != null) {
            BigDecimal billAmount = existingBill.getAmount() != null ? existingBill.getAmount() : BigDecimal.ZERO;
            BigDecimal patientPayable = existingBill.getPatientPayable() != null ? existingBill.getPatientPayable()
                    : billAmount;
            BigDecimal covered = existingBill.getCoveredAmount() != null ? existingBill.getCoveredAmount()
                    : BigDecimal.ZERO;

            BigDecimal totalExpected = covered.add(patientPayable); // total expected amount
            BigDecimal totalPaid = calculateTotalPaidForBilling(existingBill.getId()).add(amount); // total paid amount

            if (totalPaid.compareTo(totalExpected) >= 0) {
                existingBill.setStatus(BillingStatus.PAID);
                existingBill.setPaidAt(LocalDateTime.now());
            } else {
                existingBill.setStatus(BillingStatus.PENDING);
            }

            billingRepository.save(existingBill);
        }
        // ✅ Return all payments for this invoice
        return paymentRepository.findByInvoiceId(existingInvoice.getId())
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());

    }

    // converts each PaymentDto to its BigDecimal amount.
    // .reduce(BigDecimal.ZERO, BigDecimal::add) sums all amounts.
    private BigDecimal calculateTotalPaidForBilling(Long id) {
        return paymentRepository.findByBillingId(id)
                .stream()
                .map(PaymentMapper::toDto)
                .map(PaymentDto::getAmount) // extract BigDecimal amount
                // .filter(amount -> amount != null)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long id) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return PaymentMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByInvoiceId(Long invoiceId) {
        List<Payment> existing = paymentRepository.findByInvoiceId(invoiceId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("No payments found for invoice id: " + invoiceId);
        }
        return PaymentMapper.toDto(existing.get(existing.size() - 1)); // last payment
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByBillingId(Long billingId) {
        List<Payment> existing = paymentRepository.findByBillingId(billingId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("No payments found for billing id: " + billingId);
        }
        return PaymentMapper.toDto(existing.get(existing.size() - 1)); // last payment by billing id
    }

    @Override
    public void deletePayment(Long id) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        paymentRepository.delete(existing);
    }
}
