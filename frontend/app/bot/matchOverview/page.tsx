"use client";

import Pagination from "../../components/Pagination";
import LeftPageHeader from "../../components/leftPageHeader";
import Robot, { RobotType } from "../../components/robot";
import { useState, useEffect } from "react";
import MatchOverviewEntry from "./MatchOverviewEntry";
import { MatchOverviewEntryType } from "../../types/MatchOverviewEntryType";
import {
  getBotMatchOverviewPage,
  getTotalNumberOfBotMatchOverviewPages,
} from "../../services/BotService";

export default function MatchOverview(): JSX.Element {
  const [matchOverviewEntries, setMatchOverviewEntries] = useState<
    MatchOverviewEntryType[]
  >([]);
  const [pageNumber, setPageNumber] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(2);
  const entriesPerPage = 5;

  const handlePageChange = (page: number): void => {
    setPageNumber(page);

    getBotMatchOverviewPage(entriesPerPage, page)
      .then((entries) => {
        setMatchOverviewEntries(entries);
      })
      .catch(() => {});
  };

  useEffect(() => {
    getTotalNumberOfBotMatchOverviewPages(entriesPerPage)
      .then((pages) => {
        setTotalPages(pages);
      })
      .catch(() => {});
    getBotMatchOverviewPage(entriesPerPage, pageNumber)
      .then((entries) => {
        setMatchOverviewEntries(entries);
      })
      .catch(() => {});
  });

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Bot name" />

      <div className="flex flex-1 justify-center items-center pt-20">
        <div className="w-1/4 p-12">
          <Robot type={RobotType.MAGNIFIER} />
        </div>

        <div className="w-1/2 px-12 pt-16">
          <div className="flex flex-col items-center justify-center space-y-10">
            {matchOverviewEntries.map((entry, index) => (
              <MatchOverviewEntry key={index} entry={entry} />
            ))}
            <Pagination
              totalNumberOfPages={totalPages}
              onPageChange={handlePageChange}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
