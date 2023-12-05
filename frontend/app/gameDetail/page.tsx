import LeftPageHeader from "../components/leftPageHeader";
import Slider from "./Slider";
import RoundLogContainer from "./roundLogContainer";
import Card, { CardColor, CardValue } from "../components/card";
import BotHandsContainer from "./botHandsContainer";
import { cardType } from "../types/cardType";

interface Props {
  date: string;
  place: number;
}

export default function GameDetail({ date, place }: Props): JSX.Element {
  const drawpile = [
    { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
    { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
  ];
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

  const botHandExamples = [
    {
      botName: "Bot1",
      hand: [
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      ],
    },
    {
      botName: "Bot2LongLongNameIshBlublBul",
      hand: [
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GREEN, cardValue: CardValue.FIVE },
      ],
    },
    {
      botName: "Bot3",
      hand: [
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
        { cardColor: CardColor.CYAN, cardValue: CardValue.FIVE },
      ],
    },
    {
      botName: "Bot4",
      hand: [
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.ORANGE, cardValue: CardValue.FIVE },
      ],
    },
    {
      botName: "Bot5",
      hand: [
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
        { cardColor: CardColor.GRAY, cardValue: CardValue.FIVE },
      ],
    },
    {
      botName: "Bot6",
      hand: [
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
        { cardColor: CardColor.PURPLE, cardValue: CardValue.FIVE },
      ],
    },
  ];

  return (
    <div className="flex flex-col h-full">
      <h1
        id="leftHeading"
        className="absolute text-5xl md:text-7xl mt-6 md:mt-12 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit"
      >
        Game XX.YY.ZZZZ - X. Place
      </h1>

      <div className="flex flex-1 w-full pt-40">
        <div className="w-2/6 px-12">
          <RoundLogContainer logLines={logExamples} />
        </div>
        <div className="w-full">
          <div className="w-full px-12">
            <div className="w-full flex">
              <div className="w-4/6 flex justify-between grow space-x-5">
                <Slider totalRoundNumber={5} />

                <div className="flex items-center space-x-4">
                  <p className="text-4xl font-bold whitespace-nowrap">
                    Top Card:
                  </p>
                  <Card
                    value={CardValue.FOUR}
                    color={CardColor.PURPLE}
                    className="h-16 w-fit"
                  />
                </div>
              </div>
            </div>
            <div className="flex flex-col pt-5">
              <p className="text-4xl font-bold underline">Bot Hands:</p>
              <div>
                {botHandExamples.map((bot, index) => (
                  <BotHandsContainer
                    botHand={bot.hand}
                    botName={bot.botName}
                    key={index}
                  />
                ))}
              </div>
            </div>

            <hr className="my-2 h-0.5 border-t-0 bg-text opacity-100 dark:opacity-50" />

            <div className="">
              <div className="flex flex-row text-4xl py-2 items-center font-bold">
                <p>Draw Pile:</p>
                {drawpile.map((card, index) => (
                  <div>
                    <Card
                      value={card.cardValue}
                      color={card.cardColor}
                      className="h-16 w-fit px-2"
                    />
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
