import { HiPlus } from "react-icons/hi";
import SettingsForm from "./settingsForm";

export default function Settings() {
  return (
    <div className="h-full grid gridcols-3 gap-4">
      <div className="absolute text-8xl mt-16 ml-16 font-medium tracking-wider inline-block w-fit">
        Settings
      </div>
      <div className="h-full flex items-center justify-center">
        <div className="bg-secondary w-5/12 rounded-xl mt-48">
          <div className="w-full relative">
            <div className="w-40 h-40 absolute -right-40 -top-32">
              <div className="h-full">
                <img
                  src="https://www.creative-tim.com/learning-lab/tailwind-starter-kit/img/team-2-800x800.jpg"
                  alt="Profile Image"
                  className="shadow rounded-full max-w-full h-auto align-middle border-none"
                />
              </div>
              <div className="bottom-0 right-0 absolute bg-primary w-11 h-11 rounded-full flex items-center justify-center">
                <HiPlus size={25} style={{ fill: "#FEF9EC" }} />
              </div>
            </div>
          </div>
          <SettingsForm />
        </div>
      </div>
    </div>
  );
}
