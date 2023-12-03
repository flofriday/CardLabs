import Image from "next/image";

interface Props {
  title: string;
  src?: string | undefined;
  alt?: string | undefined;
  center?: boolean; // centers the image
  imgAtEnd?: boolean; // places the image at the end of the textbox
  children: React.ReactNode;
  height?: number; // will not effect the size but the resolution
  width?: number; // will not effect the size but the resolution
  imgClassName?: string; // use this to change the size of the image
  customImgElement?: React.ReactNode;
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
  customImgElement,
}: Props): JSX.Element {
  return (
    <div
      className={`bg-secondary p-6 rounded-xl text-xl flex
        ${imgAtEnd ? " justify-between" : ""}`}
    >
      {((src != null && alt != null) || customImgElement !== undefined) &&
        !imgAtEnd && (
          <div
            className={
              imgClassName +
              ` mr-5 relative flex ${center ? "items-center" : "items-start"}`
            }
          >
            {src != null && alt != null && (
              <Image
                src={src}
                alt={alt}
                className="shadow align-middle border-none"
                width={width}
                height={height}
                objectFit="contain"
              />
            )}
            {customImgElement}
          </div>
        )}
      <div>
        <h1 className="text-4xl mb-6">{title}</h1>
        {children}
      </div>
      {((src != null && alt != null) || customImgElement !== undefined) &&
        imgAtEnd && (
          <div
            className={
              imgClassName +
              ` ml-5 relative flex ${center ? "items-center" : "items-start"}`
            }
          >
            {src != null && alt != null && (
              <Image
                src={src}
                alt={alt}
                className="shadow align-middle border-none"
                width={width}
                height={height}
                objectFit="contain"
              />
            )}
            {customImgElement}
          </div>
        )}
    </div>
  );
}
