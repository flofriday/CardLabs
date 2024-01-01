import { MatchOverviewEntryType } from "@/app/types/MatchOverviewEntryType";

interface Props {
  entry: MatchOverviewEntryType;
}

export default function MatchOverviewEntry({ entry }: Props): JSX.Element {
  return (
    <div className="flex items-center justify-between p-4 bg-secondary rounded-md font-bold w-full">
      <div className="flex flex-wrap w-full items-center">
        <p className="flex-1 ml-2 pr-2 text-3xl text-center sm:text-lg md:text-xl lg:text-2xl xl:text-3xl">
          {entry.gameDate.toLocaleDateString()}
        </p>
        <p className="ml-2 text-3xl text-center">|</p>
        <p className="flex-1 ml-2 text-3xl text-center sm:text-lg md:text-xl lg:text-2xl xl:text-3xl">
          {entry.place}. Place
        </p>
        <p className="ml-2 text-3xl text-center">|</p>
        <p className="flex-1 ml-2 text-xl opacity-50 text-center sm:text-base md:text-lg lg:text-xl xl:text-2xl">
          {" "}
          Version: {entry.botVersion}
        </p>
        <p className="ml-2 text-3xl text-center">|</p>
        <p className="flex-1 ml-2 text-xl opacity-50 text-center sm:text-base md:text-lg lg:text-xl xl:text-2xl">
          {entry.scoreDevelopment} RP
        </p>
        <p className="mx-4 text-3xl text-center">|</p>
        <button className="btn bg-primary p-2 font-bold rounded-lg shadow-md text-xl hover:bg-primary_highlight">
          View
        </button>
      </div>
    </div>
  );
}
