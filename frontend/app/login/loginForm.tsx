export default function LoginForm(): JSX.Element {
  return (
    <div className="w-full h-full">
      <form className="m-12 text-2xl xl:text-4xl font-regular">
        <div className="flex justify-between">
          <div className="flex flex-col space-y-7 xl:space-y-6 h-full">
            <label htmlFor="username">Username:</label>
            <label htmlFor="password">Password:</label>
          </div>
          <div className="flex flex-col space-y-4 w-full ml-8">
            <input
              id="username"
              name="username"
              type="text"
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
            <input
              id="password"
              name="password"
              type="password"
              className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            />
          </div>
        </div>
        <div className="flex justify-around pt-4 mt-10">
          <button className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg">
            Login
          </button>
        </div>
      </form>
    </div>
  );
}
