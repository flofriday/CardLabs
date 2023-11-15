"use client";

import SettingsForm from "./settingsForm";
import ProfilePicture from "./profilePicture";
import LeftPageHeader from "../components/leftPageHeader";

export default function Settings() {
  return (
    <div className="h-full">
      <LeftPageHeader title="Settings" />
      <div className="h-full flex items-center justify-center">
        <div className="bg-secondary rounded-xl">
          <div className="w-full relative">
            <div className="2xl:w-40 2xl:h-40 h-32 w-32 absolute 2xl:-right-40 2xl:-top-32 xl:-right-32 xl:-top-24 -right-0 -top-40">
              <ProfilePicture />
            </div>
          </div>
          <SettingsForm />
        </div>
      </div>
    </div>
  );
}
