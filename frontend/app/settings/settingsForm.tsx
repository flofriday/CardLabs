import { useEffect, useState } from "react";
import DeleteAccountModal from "./deleteAccountModal";
import { type User, getUserInfo, updateUser } from "../services/UserService";
import DropDown from "../components/DropDown";
import { getLocations } from "../services/LocationService";

function saveSettings(e: React.SyntheticEvent): void {
  e.preventDefault();

  const target = e.target as typeof e.target & {
    sendScoreUpdate: { checked: boolean };
    sendWebsiteUpdate: { checked: boolean };
    sendNewsletter: { checked: boolean };
  };

  const sendScoreUpdate = target.sendScoreUpdate.checked;
  const sendWebsiteUpdate = target.sendWebsiteUpdate.checked;
  const sendNewsletter = target.sendNewsletter.checked;

  updateUser(location, sendScoreUpdate, sendWebsiteUpdate, sendNewsletter)
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
  const [sendChangeUpdates, setSendChangeUpdates] = useState(false);
  const [sendNewsletter, setSendNewsletter] = useState(false);
  const [sendScoreUpdates, setSendScoreUpdates] = useState(false);

  const [locations, setLocations] = useState<string[]>([]);
  useEffect(() => {
    getUserInfo()
      .then((u) => {
        location = u.location;
        setLocation_(location ?? locationNotSet);
        setUsername(u.username);
        setEmail(u.email);
        setSendChangeUpdates(u.sendChangeUpdates);
        setSendNewsletter(u.sendNewsletter);
        setSendScoreUpdates(u.sendScoreUpdates);
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
        <div className="ml-10 xl:ml-24 text-xl xl:text-2xl pt-3">
          <div className="flex space-x-3 align-middle">
            <input
              type="checkbox"
              id="sendScoreUpdate"
              name="sendScoreUpdate"
              checked={sendScoreUpdates}
              onChange={() => {
                setSendScoreUpdates(!sendScoreUpdates);
              }}
            />
            <label htmlFor="sendScoreUpdate">
              Send score update notifications
            </label>
          </div>
          <div className="flex space-x-3 align-middle">
            <input
              type="checkbox"
              id="sendWebsiteUpdate"
              name="sendWebsiteUpdate"
              checked={sendChangeUpdates}
              onChange={() => {
                setSendChangeUpdates(!sendChangeUpdates);
              }}
            />
            <label htmlFor="sendWebsiteUpdate">
              Send website update and changelog notifications
            </label>
          </div>
          <div className="flex space-x-3 align-middle">
            <input
              type="checkbox"
              id="sendNewsletter"
              name="sendNewsletter"
              checked={sendNewsletter}
              onChange={() => {
                setSendNewsletter(!sendNewsletter);
              }}
            />
            <label htmlFor="sendNewsletter">Send newsletter</label>
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
          <button className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg">
            Save
          </button>
        </div>
      </form>
    </>
  );
}
