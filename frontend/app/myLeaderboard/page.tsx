"use client";

import Pagination from "../components/Pagination";
import RegionSelector from "../components/RegionSelector";
import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";
import LeaderBoardEntry from "../components/LeaderBoardEntry";
import { useState, useEffect } from "react";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import { getLeaderBoardPage } from "../services/LeaderBoardService";
import { RegionType } from "../types/RegionType";
import { LeaderBoardType } from "../types/LeaderBoardType";
import { getUserInfo } from "../services/UserService";
import Link from "next/link";

export default function MyLeaderboard(): JSX.Element {
  const entriesPerPage = 5;
  const [leaderBoardEntries, setLeaderBoardEntries] = useState<
    leaderBoardEntry[]
  >([]);
  const [pageNumber, setPageNumber] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(2);
  const [selectedRegion, setSelectedRegion] = useState(RegionType.GLOBAL);
  const [userId, setUserId] = useState(-1);

  useEffect(() => {
    getUserInfo()
      .then((u) => {
        setUserId(u.id);
      })
      .catch(() => {});
  });

  useEffect(() => {
    getLeaderBoardPage(
      entriesPerPage,
      pageNumber,
      selectedRegion,
      LeaderBoardType.MY_BOTS,
      userId
    )
      .then((p) => {
        setLeaderBoardEntries(p.content);
        setTotalPages(p.totalPages);
      })
      .catch(() => {});
  }, [pageNumber, selectedRegion, userId]);

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="My Leaderboard" />

      <div className="flex flex-1 justify-center items-center pt-20">
        <div className="w-1/4 p-12">
          <Robot type={RobotType.TROPHY} />
        </div>

        <div className="w-1/2 px-12 pt-16">
          {leaderBoardEntries !== undefined && leaderBoardEntries.length > 0 ? (
            <div className="flex flex-col items-center justify-center space-y-10">
              {leaderBoardEntries.map((entry, index) => (
                <LeaderBoardEntry key={index} entry={entry} />
              ))}
              <Pagination
                totalNumberOfPages={totalPages}
                initalPage={pageNumber}
                onPageChange={(e) => {
                  setPageNumber(e);
                }}
              />
            </div>
          ) : (
            <div className="flex flex-col items-center justify-center">
              <h2 className="text-8xl font-extrabold text-center mb-6">
                No scored bots here yet :)
              </h2>
              <Link
                href="/bot/editor"
                className="btn bg-primary p-4 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
                id="button_create_new_bot"
              >
                Add a bot now
              </Link>
            </div>
          )}
        </div>

        <div className="w-1/4 p-12">
          <RegionSelector
            onRegionChange={(r) => {
              setSelectedRegion(r);
            }}
          />
        </div>
      </div>
    </div>
  );
}
