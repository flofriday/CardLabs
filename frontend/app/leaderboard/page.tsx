import LeftPageHeader from "../components/leftPageHeader";
import Robot, { RobotType } from "../components/robot";

export default function Leaderboard(): JSX.Element {
  return (
    <div className="h-full relative">
      <LeftPageHeader title="Leaderboard" />

      <Robot type={RobotType.QUESTIONMARK} />
    </div>
  );
}
