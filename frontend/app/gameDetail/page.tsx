import LeftPageHeader from "../components/leftPageHeader";

interface Props {
  date: string;
  place: number;
}

export default function GameDetail({ date, place }: Props): JSX.Element {
  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Game XX.YY.ZZZZ - X. Place" />
    </div>
  );
}
