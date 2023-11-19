import { HiPlus } from "react-icons/hi";
import Image from "next/image";

export default function Settings(): JSX.Element {
  return (
    <>
      <div className="h-full">
        <Image
          src="/example_profile_pic.jpg"
          alt="Profile Image"
          className="shadow rounded-full max-w-full h-auto align-middle border-none"
          width={500}
          height={500}
        />
      </div>
      <button className="bottom-0 right-0 absolute bg-primary w-11 h-11 rounded-full flex items-center justify-center shadow-md">
        <HiPlus size={25} style={{ fill: "#FEF9EC" }} />
      </button>
    </>
  );
}
