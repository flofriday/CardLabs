"use client";
import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";
import MiniLeaderBoard from "./miniLeaderBoard";
import Link from "next/link";
import {
  getGlobalTop5LeaderBoardEntries,
  getMyTop5LeaderBoardEntries,
} from "../services/LeaderBoardService";
import { LeaderBoardType } from "../types/LeaderBoardType";

export default function Dashboard(): JSX.Element {
  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Dashboard" />

      <div className="flex flex-1 w-full justify-center items-center pt-20">
        <div className="w-1/4 p-12">
          <Robot type={RobotType.BASIC} />
        </div>

        <div className="flex flex-col justify-center items-center space-y-16 pt-10 w-full h-full">
          <Link
            href="/bot/editor"
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
            id="button_create_new_bot"
          >
            New Bot
          </Link>
          <Link
            href="/bot"
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
          >
            My Bots
          </Link>
          <Link
            href="/rules"
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
          >
            Game Rules
          </Link>
          <Link
            href="/help"
            className="btn bg-primary flex justify-center items-center h-20 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
          >
            Help
          </Link>
        </div>

        <div className="w-2/6 pl-12">
          <div className="space-y-5 flex flex-col justify-center items-center w-full w-1/4 mr-12">
            <MiniLeaderBoard
              leaderboardType={LeaderBoardType.MY_BOTS}
              entryFetchFunction={getMyTop5LeaderBoardEntries}
            />
            <MiniLeaderBoard
              leaderboardType={LeaderBoardType.ALL_BOTS}
              entryFetchFunction={getGlobalTop5LeaderBoardEntries}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
