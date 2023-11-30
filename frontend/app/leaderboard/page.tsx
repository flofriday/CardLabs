import RegionSelector from "../components/RegionSelector";
import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";

export default function Leaderboard(): JSX.Element {
  return (
    <div className="h-full relative">
      <LeftPageHeader title="Leaderboard" />

      <Robot type={RobotType.QUESTIONMARK} />

      <div className="absolute top-0 right-0 space-y-5 flex flex-col justify-center items-center h-full w-1/4 mr-12">
        <RegionSelector />
      </div>
    </div>
  );
}
