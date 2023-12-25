import { MatchOverviewEntryType } from "@/app/types/MatchOverviewEntryType";

interface Props {
  entry: MatchOverviewEntryType;
}

export default function MatchOverviewEntry({ entry }: Props): JSX.Element {
  return (
    <div className="flex items-center justify-between p-4 bg-secondary rounded-md font-bold w-full">
      <div className="flex w-full h-full items-center">
        <p className="flex-1 ml-2 pr-2 text-3xl">
          {entry.gameDate.toLocaleDateString()}
        </p>
        <p className="flex-1 ml-2 text-3xl"> | {entry.place}. Place</p>
        <p className="ml-2 text-3xl"> |</p>
        <p className="flex-1 ml-2 text-xl"> Version: {entry.botVersion}</p>
        <p className="ml-2 text-3xl"> |</p>
        <p className="flex-1 ml-2 text-xl">{entry.scoreDevelopment} RP</p>
        <p className="mx-4 text-3xl"> |</p>
        <button className="btn bg-primary p-2 font-bold rounded-lg shadow-md text-xl  hover:bg-primary_highlight">
          View
        </button>
      </div>
    </div>
  );
}
