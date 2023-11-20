import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";

export default function Help(): JSX.Element {
  return (
    <div className="h-full relative">
      <LeftPageHeader title="Dashboard" />

      <Robot type={RobotType.QUESTIONMARK} />

      <div className="flex flex-col justify-center items-center space-y-20 pt-10 w-full h-full">
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl">
          New Bot
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl">
          My Bots
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl">
          Game Rules
        </button>
        <button className="btn bg-primary  h-24 py-2 w-1/4 font-bold rounded-lg shadow-md text-4xl">
          Help
        </button>
      </div>
    </div>
  );
}
