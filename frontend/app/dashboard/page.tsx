import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";
import MiniLeaderBoard from "./miniLeaderBoard";

export default function Dashboard(): JSX.Element {
  return (
    <div className="h-full relative">
      <LeftPageHeader title="Dashboard" />

      <Robot type={RobotType.QUESTIONMARK} />

      <div className="flex flex-col justify-center items-center space-y-20 pt-10 w-full h-full">
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight">
          New Bot
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight">
          My Bots
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight">
          Game Rules
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl hover:bg-primary_highlight">
          Help
        </button>
      </div>

      <div className="absolute right-0 flex-col h-full w-1/4">
        <MiniLeaderBoard heading="My Bots" />
        <MiniLeaderBoard heading="Global Ranking" />
      </div>
    </div>
  );
}
