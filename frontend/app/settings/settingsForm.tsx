import { useEffect, useState } from "react";
import DeleteAccountModal from "./deleteAccountModal";
import { getUserInfo, updateUser } from "../services/UserService";
import DropDown from "../components/DropDown";
import { getLocations } from "../services/LocationService";

function saveSettings(e: React.SyntheticEvent): void {
  e.preventDefault();

  updateUser(location)
    .then(() => {})
    .catch(() => {});
}
let location: string | null = null;
const locationNotSet = "Unkown / Not set";
function setLocation(loc: string): void {
  if (loc === locationNotSet) {
    location = null;
  } else {
    location = loc;
  }
}

export default function SettingsForm(): JSX.Element {
  const [deleteAccountModalVisiblity, setDeleteAccountModalVisiblity] =
    useState(false);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [location_, setLocation_] = useState("");

  const [locations, setLocations] = useState<string[]>([]);
  useEffect(() => {
    getUserInfo()
      .then((u) => {
        location = u.location;
        setLocation_(location ?? locationNotSet);
        setUsername(u.username);
        setEmail(u.email);
      })
      .catch(() => {});
    getLocations()
      .then((l) => {
        l.unshift("Unkown / Not set");
        setLocations(l);
      })
      .catch(() => {});
  }, []);

  return (
    <>
      {deleteAccountModalVisiblity && (
        <DeleteAccountModal
          onClose={() => {
            setDeleteAccountModalVisiblity(false);
          }}
        />
      )}

      <form
        className="m-12 text-2xl xl:text-4xl font-regular"
        onSubmit={saveSettings}
      >
        <div className="flex justify-between">
          <div className="flex flex-col space-y-7 xl:space-y-6 h-full">
            <label htmlFor="username">Username:</label>
            <label htmlFor="e-mail">E-Mail:</label>
            <label htmlFor="location">Location:</label>
          </div>
          <div className="flex flex-col space-y-4 w-full ml-8">
            <input
              id="username"
              name="username"
              type="text"
              value={username}
              disabled={true}
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
            <input
              id="e-mail"
              name="e-mail"
              type="text"
              value={email}
              disabled={true}
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
            <DropDown
              values={locations}
              className="w-full"
              defaultValue={location_}
              onChange={(l) => {
                setLocation(l);
                setLocation_(l);
              }}
            />
          </div>
        </div>
        <div className="flex justify-around pt-4">
          <button
            className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
            onClick={() => {
              setDeleteAccountModalVisiblity(true);
            }}
            type="button"
          >
            Delete Account
          </button>
          <button
            className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
            id="save_settings_button"
          >
            Save
          </button>
        </div>
      </form>
    </>
  );
}
