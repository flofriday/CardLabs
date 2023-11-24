import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";
import MiniLeaderBoard from "./miniLeaderBoard";
import Link from "next/link";

export default function Dashboard(): JSX.Element {
  return (
    <div className="h-full relative">
      <LeftPageHeader title="Dashboard" />

      <Robot type={RobotType.QUESTIONMARK} />

      <div className="flex flex-col justify-center items-center space-y-16 pt-10 w-full h-full">
        <Link
          href="/" // TODO add link to new bot page here
          className="btn bg-primary flex justify-center items-center h-20 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight"
        >
          New Bot
        </Link>
        <Link
          href="/" // TODO add link to my bots page here
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

      <div className="absolute top-0 right-0 space-y-5 flex flex-col justify-center items-center h-full w-1/4 mr-12">
        <MiniLeaderBoard heading="My Bots" />
        <MiniLeaderBoard heading="Global Ranking" />
      </div>
    </div>
  );
}
