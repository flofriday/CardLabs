interface Props {
  title: string;
}

export default function LeftPageHeader(props: Props): JSX.Element {
  return (
    <h1 className="absolute text-6xl md:text-8xl mt-10 md:mt-16 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit">
      {props.title}
    </h1>
  );
}
