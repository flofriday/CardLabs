import LeftPageHeader from "../components/leftPageHeader";
import BotCard from "./botCard";

export default function BotOverview(): JSX.Element {
  return (
    <>
      <LeftPageHeader title="My Bots" margin={5} />
      <div className="h-full w-full flex items-center justify-center">
        <div className="h-4/6 w-9/12 grid grid-cols-1 lg:grid-cols-2 2xl:grid-cols-3 gap-8">
          <BotCard />
          <BotCard />
          <BotCard />
          <BotCard />
          <BotCard />
          <BotCard />
        </div>
      </div>
    </>
  );
}
