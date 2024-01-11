import { leaderBoardEntry } from "../types/leaderBoardEntry";

interface Props {
  entry: leaderBoardEntry;
}

export default function LeaderBoardEntry({ entry }: Props): JSX.Element {
  return (
    <div className="flex items-center justify-between p-4 bg-secondary rounded-md text-4xl font-bold w-full">
      <div className="flex w-full">
        <p
          className={`flex-1 font-bold truncate pr-2
          ${
            entry.place === 1
              ? " text-gold"
              : entry.place === 2
              ? " text-silver"
              : entry.place === 3
              ? " text-bronze"
              : " text-text"
          }
          `}
        >
          {entry.place + "."}
        </p>
        <p className="flex-1 font-bold truncate pr-2"> {entry.botName} </p>
        <p className="flex-1 ml-2">{entry.userName}</p>
        <p className="flex-1 ml-2">{entry.score}</p>
      </div>
    </div>
  );
}
