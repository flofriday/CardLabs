import { HiPlus } from "react-icons/hi";
import Image from "next/image";
import { getUserProfilePicture } from "../services/UserService";

interface Props {
  changeButton?: boolean;
}

export default function ProfilePicture({
  changeButton = false,
}: Props): JSX.Element {
  return (
    <>
      <div className="h-full">
        <Image
          src={getUserProfilePicture()}
          alt="Profile Image"
          className="shadow rounded-full max-w-full h-auto align-middle border-none bg-text"
          width={500}
          height={500}
        />
      </div>
      <button
        className={`${
          changeButton ? "" : "hidden"
        } bottom-0 right-0 absolute bg-primary w-11 h-11 rounded-full flex items-center justify-center shadow-md`}
      >
        <HiPlus size={25} style={{ fill: "#FEF9EC" }} />
      </button>
    </>
  );
}
