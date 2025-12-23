"use client";

import { useState } from "react";
import { ExamResultDto } from "@/types";
import { ChevronLeft, ChevronRight, ArrowUpDown } from "lucide-react";
import clsx from "clsx";

interface ResultsTableProps {
  data: ExamResultDto[];
}

type SortField = 'studentName' | 'subjectName' | 'marksObtained' | 'percentage' | 'examDate';
type SortDirection = 'asc' | 'desc';

export function ResultsTable({ data }: ResultsTableProps) {
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortField, setSortField] = useState<SortField>('examDate');
  const [sortDirection, setSortDirection] = useState<SortDirection>('desc');
  const [filterText, setFilterText] = useState("");

  // Handling Sorting
  const sortedData = [...data].sort((a, b) => {
    let aValue = a[sortField];
    let bValue = b[sortField];

    if (typeof aValue === 'string') aValue = aValue.toLowerCase();
    if (typeof bValue === 'string') bValue = bValue.toLowerCase();

    if (aValue < bValue) return sortDirection === 'asc' ? -1 : 1;
    if (aValue > bValue) return sortDirection === 'asc' ? 1 : -1;
    return 0;
  });

  // Handling Filtering
  const filteredData = sortedData.filter(item => 
    item.studentName.toLowerCase().includes(filterText.toLowerCase()) ||
    item.subjectName.toLowerCase().includes(filterText.toLowerCase())
  );

  // Handling Pagination
  const totalPages = Math.ceil(filteredData.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const paginatedData = filteredData.slice(startIndex, startIndex + itemsPerPage);

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortDirection('asc');
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <input 
          type="text" 
          placeholder="Search by student or subject..." 
          className="max-w-sm px-3 py-2 border rounded-md text-sm bg-background"
          value={filterText}
          onChange={(e) => { setFilterText(e.target.value); setCurrentPage(1); }}
        />
        <div className="text-sm text-muted-foreground">
            Total Results: {filteredData.length}
        </div>
      </div>

      <div className="rounded-md border bg-card">
        <div className="relative w-full overflow-auto">
          <table className="w-full caption-bottom text-sm text-left">
            <thead className="[&_tr]:border-b">
              <tr className="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground cursor-pointer hover:text-foreground" onClick={() => handleSort('studentName')}>
                  <div className="flex items-center">Student Name <ArrowUpDown className="ml-2 h-4 w-4" /></div>
                </th>
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground cursor-pointer hover:text-foreground" onClick={() => handleSort('subjectName')}>
                   <div className="flex items-center">Subject <ArrowUpDown className="ml-2 h-4 w-4" /></div>
                </th>
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground cursor-pointer hover:text-foreground" onClick={() => handleSort('marksObtained')}>
                   <div className="flex items-center">Marks <ArrowUpDown className="ml-2 h-4 w-4" /></div>
                </th>
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground cursor-pointer hover:text-foreground" onClick={() => handleSort('percentage')}>
                   <div className="flex items-center">Percentage <ArrowUpDown className="ml-2 h-4 w-4" /></div>
                </th>
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground">Status</th>
                <th className="h-12 px-4 align-middle font-medium text-muted-foreground cursor-pointer hover:text-foreground" onClick={() => handleSort('examDate')}>
                   <div className="flex items-center">Date <ArrowUpDown className="ml-2 h-4 w-4" /></div>
                </th>
              </tr>
            </thead>
            <tbody className="[&_tr:last-child]:border-0">
              {paginatedData.length > 0 ? (
                paginatedData.map((result, index) => (
                  <tr key={index} className="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                    <td className="p-4 align-middle font-medium">{result.studentName}</td>
                    <td className="p-4 align-middle">{result.subjectName}</td>
                    <td className="p-4 align-middle">{result.marksObtained} / {result.totalMarks}</td>
                    <td className="p-4 align-middle">{result.percentage.toFixed(1)}%</td>
                    <td className="p-4 align-middle">
                      <span className={clsx(
                        "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
                        result.status === 'PASSED' 
                          ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300" 
                          : "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300"
                      )}>
                        {result.status}
                      </span>
                    </td>
                    <td className="p-4 align-middle">{result.examDate}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={6} className="h-24 text-center">
                    No results found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div className="flex items-center justify-end space-x-2 py-4">
        <button
          className="inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 border border-input bg-background shadow-sm hover:bg-accent hover:text-accent-foreground h-9 px-4 py-2"
          onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
          disabled={currentPage === 1}
        >
          <ChevronLeft className="h-4 w-4 mr-2" />
          Previous
        </button>
        <div className="text-sm font-medium">
            Page {currentPage} of {totalPages || 1}
        </div>
        <button
          className="inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 border border-input bg-background shadow-sm hover:bg-accent hover:text-accent-foreground h-9 px-4 py-2"
          onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
          disabled={currentPage === totalPages || totalPages === 0}
        >
          Next
          <ChevronRight className="h-4 w-4 ml-2" />
        </button>
      </div>
    </div>
  );
}
