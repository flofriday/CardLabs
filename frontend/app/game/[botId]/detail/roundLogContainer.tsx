import { LogLine } from "@/app/types/LogLine";

interface Props {
  logLines: LogLine[];
}

export default function RoundLogContainer({ logLines }: Props): JSX.Element {
  return (
    <div className="bg-secondary p-4 text-text rounded-lg overflow-y-auto max-h-[74vh]">
      <h2 className="text-4xl font-bold mb-4">Log: </h2>
      {logLines.map((log, index) => (
        <pre
          key={index}
          className={`mb-2 ${
            log.type !== "system" ? "text-yellow" : ""
          } p-2 rounded-md`}
        >
          {log.type !== "system" && "[DBG] "}
          {log.message.trim()}
        </pre>
      ))}
    </div>
  );
}
