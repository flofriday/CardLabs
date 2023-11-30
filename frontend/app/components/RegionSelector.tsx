export default function RegionSelector(): JSX.Element {
  return (
    <div className="bg-secondary rounded-lg w-full">
      <h1 className="text-4xl font-bold p-6">Region</h1>
      <div className="flex flex-col space-y-4 p-6">
        <button className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight">
          Global
        </button>
        <button className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight">
          Content
        </button>
        <button className="btn bg-primary py-2 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight">
          Country
        </button>
      </div>
    </div>
  );
}
