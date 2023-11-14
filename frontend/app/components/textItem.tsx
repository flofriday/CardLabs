import Image from "next/image";

interface Props {
  title: string;
  src?: string;
  alt?: string;
  center?: boolean; //centers the image
  imgAtEnd?: boolean; //places the image at the end of the textbox
  children: React.ReactNode;
  height?: number; //will not effect the size but the resolution
  width?: number; //will not effect the size but the resolution
  imgClassName?: string; //use this to change the size of the image
}

export default function TextItem({
  title,
  src,
  alt,
  imgAtEnd = false,
  children,
  center = false,
  height = 200,
  width = 200,
  imgClassName,
}: Props) {
  return (
    <div
      className={`bg-secondary w-6/12 p-6 rounded-xl text-xl flex
        ${imgAtEnd === true ? " justify-between" : ""}`}
    >
      {src && alt && !imgAtEnd && (
        <div
          className={
            imgClassName +
            ` mr-5 relative flex ${center ? "items-center" : "items-start"}`
          }
        >
          <Image
            src={src}
            alt={alt}
            className="shadow align-middle border-none"
            width={width}
            height={height}
            objectFit="contain"
          />
        </div>
      )}
      <div>
        <h1 className="text-4xl mb-6">{title}</h1>
        {children}
      </div>
      {src && alt && imgAtEnd === true && (
        <div
          className={
            imgClassName +
            ` mr-5 relative flex ${center ? "items-center" : "items-start"}`
          }
        >
          <Image
            src={src}
            alt={alt}
            className="shadow align-middle border-none ml-5"
            width={width}
            height={height}
            objectFit="contain"
          />
        </div>
      )}
    </div>
  );
}
