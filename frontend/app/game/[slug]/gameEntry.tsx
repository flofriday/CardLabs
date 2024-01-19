import IconButton, { ButtonIcon } from "@/app/components/iconButton";
import { Game } from "@/app/types/game";

interface Props {
  game: Game;
  botId: number;
}

export default function GameEntry({ game, botId }: Props): JSX.Element {
  return (
    <div className="flex items-center justify-between p-4 bg-secondary rounded-md text-4xl font-bold w-full">
      <div className="flex w-full justify-between">
        <p>{game.startTime.toLocaleString()}</p>
        {game.gameState === "COMPLETED" ? (
          <p>{game.winningBotId === botId ? "WIN" : "LOST"}</p>
        ) : (
          <p>RUNNING</p>
        )}
        <IconButton
          text="View"
          type={ButtonIcon.INFO}
          onClick={() => {
            console.log("forward :)");
          }}
        />
      </div>
    </div>
  );
}
