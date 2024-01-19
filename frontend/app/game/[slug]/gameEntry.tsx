interface Props {
  game: Game;
}

export default function GameEntry({ game }: Props): JSX.Element {
  return (
    <div className="flex items-center justify-between p-4 bg-secondary rounded-md text-4xl font-bold w-full">
      <div className="flex w-full">
        <p>{game.id}</p>
      </div>
    </div>
  );
}
