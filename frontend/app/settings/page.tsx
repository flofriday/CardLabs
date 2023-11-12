import SettingsForm from "./settingsForm";
import ProfilePicture from "./profilePicture";
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
              <ProfilePicture />
            </div>
          </div>
          <SettingsForm />
        </div>
      </div>
    </div>
  );
}
