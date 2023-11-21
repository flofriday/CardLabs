import { leaderBoardEntry } from "../types/leaderBoardEntry";

interface Props {
  title: string;
  entries: leaderBoardEntry[];
}

export default function MiniLeaderBoardContent({
  title,
  entries,
}: Props): JSX.Element {
  return (
    <div>
      <h2 className="text-4xl font-bold mb-4 mx-2.5">{title}</h2>
      {entries.map((entry, index) => (
        <span key={index} className="mb-4 p-4 grid grid-cols-5">
          <p className="font-bold col-span-2 truncate">
            {entry.place}. {entry.botName}
          </p>
          <div className="col-span-2 pr-4">
            <div className="bg-background h-4 w-full h-full ml-2flex items-center justify-center p-1">
              <div
                className="bg-primary_highlight h-full"
                style={{ width: `${entry.score}%` }}
              />
            </div>
          </div>
          <p className="ml-2 col-span-1 pl-1">Score: {entry.score}</p>
        </span>
      ))}
    </div>
  );
}
