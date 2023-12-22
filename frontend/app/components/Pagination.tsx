"use client";

import React, { useEffect, useState } from "react";

interface Props {
  onPageChange: (page: number) => void;
  totalNumberOfPages: number;
}

export default function Pagination({
  onPageChange,
  totalNumberOfPages,
}: Props): JSX.Element {
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(totalNumberOfPages);

  useEffect(() => {
    setTotalPages(totalNumberOfPages);
  }, [totalNumberOfPages]);

  const goToPage = (page: number): void => {
    setCurrentPage(page);
    onPageChange(page);
  };

  const goToFirstPage = (): void => {
    setCurrentPage(0);
    onPageChange(0);
  };

  const goToLastPage = (): void => {
    setCurrentPage(totalPages - 1);
    onPageChange(totalPages - 1);
  };

  const goToPrevPage = (): void => {
    const prevPage = Math.max(currentPage - 1, 0);
    setCurrentPage(prevPage);
    onPageChange(prevPage);
  };

  const goToNextPage = (): void => {
    const nextPage = Math.min(currentPage + 1, totalPages - 1);
    setCurrentPage(nextPage);
    onPageChange(nextPage);
  };

  const renderPageNumbers = (): JSX.Element[] => {
    const pages = [];
    for (let i = 1; i <= totalPages; i++) {
      pages.push(
        <button
          key={i}
          className={`mx-1 px-4 py-3 w-14 h-14 text-4xl ${
            i === currentPage + 1
              ? "bg-primary text-text font-bold"
              : "bg-secondary text-text font-normal hover:bg-primary"
          } rounded-md flex items-center justify-center`}
          onClick={() => {
            goToPage(i - 1);
          }}
        >
          {i}
        </button>
      );
    }
    return pages;
  };

  return (
    <div className="flex items-center justify-center mt-4">
      <button
        className="mx-1 px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center"
        onClick={goToFirstPage}
      >
        &lt;&lt;
      </button>
      <button
        className="mx-1 px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center"
        onClick={goToPrevPage}
      >
        &lt;
      </button>
      {renderPageNumbers()}
      <button
        className="mx-1 px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center"
        onClick={goToNextPage}
      >
        &gt;
      </button>
      <button
        className="mx-1 px-4 py-3 w-14 h-14 text-4xl bg-secondary text-text rounded-md hover:bg-primary hover:text-white flex items-center justify-center"
        onClick={goToLastPage}
      >
        &gt;&gt;
      </button>
    </div>
  );
}
