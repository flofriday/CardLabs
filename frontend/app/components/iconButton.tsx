import Image from "next/image";

interface Props {
  text: string;
  type: ButtonIcon;
  onClick?: () => void;
  id: string;
}

export enum ButtonIcon {
  COMPILE = "compile.svg",
  SAVE = "save.svg",
  TEST = "test.svg",
  RANK = "rank.svg",
  DELETE = "delete.svg",
}

export default function IconButton({
  text,
  type,
  onClick = () => {},
  id,
}: Props): JSX.Element {
  return (
    <button
      onClick={onClick}
      className="btn text-text py-2 w-36 rounded-full shadow-md text-lg outline outline-1 align-middle inline-flex items-center justify-start hover:bg-primary"
      id={id}
    >
      <Image
        src={`/buttonIcons/${type}`}
        alt=""
        width={15}
        height={15}
        className="mx-3"
      />
      <div>{text}</div>
    </button>
  );
}
