export default function SettingsForm() {
  return (
    <form className="m-12 text-2xl xl:text-4xl font-regular">
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
            className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
          />
          <input
            id="e-mail"
            name="e-mail"
            type="text"
            className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
          />
          <input
            id="location"
            name="location"
            type="text"
            className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
          />
        </div>
      </div>
      <div className="ml-10 xl:ml-24 text-xl xl:text-2xl pt-3">
        <div className="flex space-x-3 align-middle">
          <input type="checkbox" id="sendScoreUpdate" name="sendScoreUpdate" />
          <label htmlFor="sendScoreUpdate">
            Send score update notifications
          </label>
        </div>
        <div className="flex space-x-3 align-middle">
          <input
            type="checkbox"
            id="sendWebsiteUpdate"
            name="sendWebsiteUpdate"
          />
          <label htmlFor="sendWebsiteUpdate">
            Send website update and changelog notifications
          </label>
        </div>
        <div className="flex space-x-3 align-middle">
          <input type="checkbox" id="sendNewsletter" name="sendNewsletter" />
          <label htmlFor="sendNewsletter">Send newsletter</label>
        </div>
      </div>
      <div className="flex justify-around pt-4">
        <button className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg">
          Delete Account
        </button>
        <button className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg">
          Save
        </button>
      </div>
    </form>
  );
}
