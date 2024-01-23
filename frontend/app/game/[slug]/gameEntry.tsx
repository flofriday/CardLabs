import IconButton, { ButtonIcon } from "@/app/components/iconButton";
import { Game } from "@/app/types/game";
import { useRouter } from "next/navigation";

interface Props {
  game: Game;
  botId: number;
}

export default function GameEntry({ game, botId }: Props): JSX.Element {
  const router = useRouter();

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
            router.push(`/game/detail/${game.id}`);
          }}
        />
      </div>
    </div>
  );
}
