import Image from "next/image";

interface Props {
  title: string;
  picturePath?: string;
  altText?: string;
  pictureAtEnd?: boolean;
  children: React.ReactNode;
}

export default function TextItem(props: Props) {
  return (
    <div
      className={
        "bg-secondary w-6/12 p-6 rounded-xl text-xl flex " +
        (props.pictureAtEnd === true && "justify-between")
      }
    >
      {props.picturePath && props.altText && !props.pictureAtEnd && (
        <Image
          src={props.picturePath}
          alt={props.altText}
          className="shadow align-middle border-none mr-5"
          width={100}
          height={100}
        />
      )}
      <div>
        <h1 className="text-4xl mb-6">{props.title}</h1>
        {props.children}
      </div>
      {props.picturePath && props.altText && props.pictureAtEnd === true && (
        <Image
          src={props.picturePath}
          alt={props.altText}
          className="shadow align-middle border-none ml-5"
          width={100}
          height={100}
        />
      )}
    </div>
  );
}
