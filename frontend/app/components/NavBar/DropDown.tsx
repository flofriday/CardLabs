interface Props {
  className?: string;
}

export default function DropDown({ className = "" }: Props): JSX.Element {
  return (
    <div
      className={`fixed right-8 top-14 bg-primary w-56 p-3 rounded-md border-2 ${className}`}
    >
      <button className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg">
        Logout
      </button>
    </div>
  );
}
