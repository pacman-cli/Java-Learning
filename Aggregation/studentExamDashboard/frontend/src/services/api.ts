import axios from 'axios';
import {CategoryWiseStats, MonthlyStat, ExamResultDto, DashboardData, DashboardSummary} from '@/types';
// backend base url from env
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

// creating axios instance with base url and headers
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const fetchDashboardSummary = async (): Promise<DashboardSummary> => {
    const response = await api.get<DashboardSummary>(
        '/dashboard/summary'
    );

    return response.data;
};

export const fetchCategoryWiseStats = async (): Promise<CategoryWiseStats[]> => {
    const response = await api.get<CategoryWiseStats[]>(
        '/dashboard/category-wise-stats'
    );

    return response.data;
};

export const fetchDetailedExamResults = async (): Promise<ExamResultDto[]> => {
    const response = await api.get<ExamResultDto[]>(
        '/dashboard/detailedExamResults'
    );

    return response.data;
};

export const fetchDashboardOverview = async (): Promise<DashboardData> => {
    const [summary, categoryStats, examResults] = await Promise.all([
        fetchDashboardSummary(),
        fetchCategoryWiseStats(),
        fetchDetailedExamResults()
    ]);

    // Backend doesn't provide monthly stats in the controller I saw. 
    // I will generate monthly stats from examResults for the trend chart on the frontend for now
    // to satisfy the requirement "Monthly trend line chart".

    // Group by month YYYY-MM
    const monthlyGroups = examResults.reduce((acc, result) => {
        const date = new Date(result.examDate);
        const monthKey = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
        if (!acc[monthKey]) {
            acc[monthKey] = {
                month: monthKey,
                totalExams: 0,
                totalMarks: 0,
                passedCount: 0
            };
        }
        acc[monthKey].totalExams++;
        acc[monthKey].totalMarks += result.marksObtained;
        if (result.status === 'PASSED') {
            acc[monthKey].passedCount++;
        }
        return acc;
    }, {} as Record<string, any>);

    const monthlyStats: MonthlyStat[] = Object.values(monthlyGroups).map((group: any) => ({
        month: group.month,
        totalExams: group.totalExams,
        averageMarks: group.totalMarks / group.totalExams,
        passRate: (group.passedCount / group.totalExams) * 100
    })).sort(
        (
            a,
            b
        ) => a.month.localeCompare(b.month));


    return {
        summary,
        categoryStats,
        examResults,
        monthlyStats
    };
}
