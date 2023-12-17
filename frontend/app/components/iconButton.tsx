import Image from "next/image";

interface Props {
  text: string;
  type: ButtonIcon;
  onClick?: () => void;
  id?: string;
  width?: string;
  hover?: string;
}

export enum ButtonIcon {
  COMPILE = "compile.svg",
  SAVE = "save.svg",
  TEST = "test.svg",
  RANK = "rank.svg",
  DELETE = "delete.svg",
  EDIT = "compile.svg",
  INFO = "info.svg",
}

export default function IconButton({
  text,
  type,
  onClick = () => {},
  id,
  width = "w-36",
  hover = "hover:bg-primary",
}: Props): JSX.Element {
  return (
    <button
      onClick={onClick}
      className={`btn text-text py-2 ${width}  rounded-full shadow-md text-lg outline outline-1 align-middle inline-flex items-center justify-start ${hover}`}
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
