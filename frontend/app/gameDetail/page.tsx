import LeftPageHeader from "../components/leftPageHeader";
import RoundLogContainer from "./roundLogContainer";

interface Props {
  date: string;
  place: number;
}

export default function GameDetail({ date, place }: Props): JSX.Element {
  const logExamples = [
    { message: "This is a normal log message." },
    { message: "Another log entry." },
    { message: "Debugging information.", isDebug: true },
    { message: "More normal logs." },
    { message: "Debug log with additional information.", isDebug: true },
    { message: "More normal logs." },
    { message: "This is a normal log message." },
    { message: "Another log entry." },
    { message: "Debugging information.", isDebug: true },
    { message: "More normal logs." },
    { message: "Debug log with additional information.", isDebug: true },
    { message: "More normal logs." },
    { message: "This is a normal log message." },
    { message: "Another log entry." },
    { message: "Debugging information.", isDebug: true },
    { message: "More normal logs." },
    { message: "Debug log with additional information.", isDebug: true },
    { message: "More normal logs." },
  ];

  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Game XX.YY.ZZZZ - X. Place" />

      <div className="flex flex-1 w-full justify-center items-center pt-32">
        <RoundLogContainer logLines={logExamples} />
      </div>
    </div>
  );
}
