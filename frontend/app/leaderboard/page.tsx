"use client";

import Pagination from "../components/Pagination";
import RegionSelector from "../components/RegionSelector";
import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";
import LeaderBoardEntry from "./LeaderBoardEntry";
import { useState, useEffect } from "react";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import {
  getLeaderBoardPage,
  getTotalNumberOfPages,
} from "../services/LeaderBoardService";
import { RegionType } from "../types/RegionType";
import { LeaderBoardType } from "../types/LeaderBoardType";

export default function Leaderboard(): JSX.Element {
  const entriesPerPage = 5;
  const [leaderBoardEntries, setLeaderBoardEntries] = useState<
    leaderBoardEntry[]
  >([]);
  const [pageNumber, setPageNumber] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(2);
  const [selectedRegion, setSelectedRegion] = useState(RegionType.GLOBAL);

  useEffect(() => {
    getTotalNumberOfPages(entriesPerPage)
      .then((pages) => {
        setTotalPages(pages);
      })
      .catch(() => {});
    getLeaderBoardPage(
      entriesPerPage,
      pageNumber,
      selectedRegion,
      LeaderBoardType.ALL_BOTS
    )
      .then((entries) => {
        setLeaderBoardEntries(entries);
      })
      .catch(() => {});
  });

  const handlePageChange = (page: number): void => {
    setPageNumber(page);

    getLeaderBoardPage(5, page, selectedRegion, LeaderBoardType.ALL_BOTS)
      .then((entries) => {
        setLeaderBoardEntries(entries);
      })
      .catch(() => {});
  };

  const handleRegionChange = (region: RegionType): void => {
    setSelectedRegion(region);

    getLeaderBoardPage(5, pageNumber, selectedRegion, LeaderBoardType.ALL_BOTS)
      .then((entries) => {
        setLeaderBoardEntries(entries);
      })
      .catch(() => {});
  };

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Leaderboard" />

      <div className="flex flex-1 justify-center items-center pt-20">
        <div className="w-1/4 p-12">
          <Robot type={RobotType.TROPHY} />
        </div>

        <div className="w-1/2 px-12 pt-16">
          <div className="flex flex-col items-center justify-center space-y-10">
            {leaderBoardEntries.map((entry, index) => (
              <LeaderBoardEntry key={index} entry={entry} />
            ))}
            <Pagination
              totalNumberOfPages={totalPages}
              onPageChange={handlePageChange}
            />
          </div>
        </div>

        <div className="w-1/4 p-12">
          <RegionSelector onRegionChange={handleRegionChange} />
        </div>
      </div>
    </div>
  );
}
