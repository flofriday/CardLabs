import Image from "next/image";

export const RobotType = {
    QUESTIONMARK: {
        src: "/robots/robot_with_question_marks.svg",
        alt: "Robot surrounded by question marks"
    }
}
type RobotType_ = (typeof RobotType)[keyof typeof RobotType];

interface Props {
  type: RobotType_
}
//  "Robot surrounded by question marks"
export default function Robot({type}: Props): JSX.Element {
  return (
    <div className="absolute w-2/12 m-12 flex h-full">
      <Image
        src={type.src}
        alt={type.alt}
        width={500}
        height={500}
      />
    </div>
  );
}
