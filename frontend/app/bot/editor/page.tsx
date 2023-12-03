import LeftPageHeader from "@/app/components/leftPageHeader";

export default function BotEditor(): JSX.Element {
  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title="Adath Wacoffin" subTitle="Bot-Name" />
      <div className="h-full w-8/12">
        <div className="bg-primary w-full mt-44 h-16">Buttons go here</div>
        <div className="w-full h-[calc(100%_-_4rem_-_11rem)] bg-secondary">
          <textarea className="h-full w-full bg-secondary p-3" />
        </div>
      </div>
      <div className="h-full w-4/12 bg-accent">Log stuff</div>
    </div>
  );
}
