import Image from "next/image";

export const RobotType = {
  QUESTIONMARK: {
    src: "/robots/robot_with_question_marks.svg",
    alt: "Robot surrounded by question marks",
  },
  RULER: {
    src: "/robots/robot_with_ruler.svg",
    alt: "Robot holding a ruler",
  },
  TROPHY: {
    src: "/robots/robot_with_trophy.svg",
    alt: "Robot holding a trophy",
  },
  WRENCH: {
    src: "/robots/robot_with_wrench.svg",
    alt: "Robot holding a wrench",
  },
  MAGNIFIER: {
    src: "/robots/robot_with_magnifier.svg",
    alt: "Robot holding a magnifier",
  },
  BASIC: {
    src: "/robots/robot.svg",
    alt: "Just a robot",
  },
};

type RobotType_ = (typeof RobotType)[keyof typeof RobotType];

interface Props {
  type: RobotType_;
}
//  "Robot surrounded by question marks"
export default function Robot({ type }: Props): JSX.Element {
  return (
    <div className="absolute w-2/12 mx-12 flex h-full">
      <Image src={type.src} alt={type.alt} width={500} height={500} />
    </div>
  );
}
