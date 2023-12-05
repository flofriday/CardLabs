import { LogLine } from "../types/LogLine";

interface Props {
  logLines: LogLine[];
}

export default function RoundLogContainer({ logLines }: Props): JSX.Element {
  let nonDebugIndex = 0;

  return (
    <div className="bg-secondary p-4 text-text rounded-lg overflow-y-auto max-h-[74vh]">
      <h2 className="text-4xl font-bold mb-4">Log: </h2>
      {logLines.map((log, index) => (
        <div
          key={index}
          className={`mb-2 ${log.isDebug ? "text-yellow" : ""} p-2 rounded-md`}
        >
          {log.isDebug && "[DBG] "} {!log.isDebug && `${++nonDebugIndex}.`}{" "}
          {log.message}
        </div>
      ))}
    </div>
  );
}
