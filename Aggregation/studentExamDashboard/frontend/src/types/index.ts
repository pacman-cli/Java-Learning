export interface CategoryWiseStats {
    subjectNames: string;
    totalExams: number;
    averageMarks: number;
    passRate: number;
}

export interface DashboardSummary {
    totalStudents: number;
    totalExams: number;
    averageMarks: number;
    passPercentage: number;
    totalPassedExams: number;
    totalFailedExams: number;
}

export interface ExamResultDto {
    id: number;
    studentName: string;
    subjectName: string;
    marksObtained: number;
    totalMarks: number;
    percentage: number;
    examDate: string; // LocalDate is serialized to string
    status: string;
}

export interface DashboardData {
    summary: DashboardSummary;
    categoryStats: CategoryWiseStats[];
    examResults: ExamResultDto[];
    monthlyStats: MonthlyStat[]; // Backend doesn't seem to have monthly stats in the controller I checked, so I will fetch it if I can or mock it for now, but user said "Monthly statistics (month, total exams, average marks, pass rate)" is returned. 
    // Wait, I didn't see a monthly stats endpoint in the controller.
}

export interface MonthlyStat {  // Keeping this for now, will double check if I missed an endpoint
    month: string;
    totalExams: number;
    averageMarks: number;
    passRate: number;
}
