import { HiPlus } from "react-icons/hi";

export default function Settings() {
  return (
    <>
      <div className="h-full">
        <img
          src="https://www.creative-tim.com/learning-lab/tailwind-starter-kit/img/team-2-800x800.jpg"
          alt="Profile Image"
          className="shadow rounded-full max-w-full h-auto align-middle border-none"
        />
      </div>
      <button className="bottom-0 right-0 absolute bg-primary w-11 h-11 rounded-full flex items-center justify-center shadow-md">
        <HiPlus size={25} style={{ fill: "#FEF9EC" }} />
      </button>
    </>
  );
}
