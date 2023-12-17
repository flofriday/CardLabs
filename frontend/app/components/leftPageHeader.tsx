interface Props {
  title: string;
  subTitle?: string;
  margin?: number;
}

export default function LeftPageHeader({
  title,
  subTitle = "",
  margin = 10,
}: Props): JSX.Element {
  return (
    <div>
      <h1
        id="leftHeading"
        className={`absolute text-6xl md:text-8xl mt-${margin} md:mt-${
          margin + 6
        } ml-${margin} md:ml-${
          margin + 6
        } font-medium tracking-wider inline-block w-fit"`}
      >
        {title}
      </h1>
      <h4 className="absolute text-l md:text-2xl mt-4 md:mt-8 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit opacity-50">
        {subTitle}
      </h4>
    </div>
  );
}
