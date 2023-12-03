interface Props {
  text: string;
}

export default function IconButton({ text }: Props): JSX.Element {
  return (
    <button className="btn text-text py-2 w-36 rounded-full shadow-md text-lg outline outline-1 align-middle inline-flex items-center justify-center">
      {text}
    </button>
  );
}
