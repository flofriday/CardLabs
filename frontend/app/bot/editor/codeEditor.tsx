interface Props {
  onChange?: (code: string | null) => void;
}

export default function CodeEditor({
  onChange = (code) => {},
}: Props): JSX.Element {
  return (
    <div className="w-full h-[calc(100%_-_4rem_-_11rem)] bg-secondary">
      <textarea
        className="h-full w-full bg-secondary p-3"
        onChange={(e) => {
          onChange(e.target.value);
        }}
      />
    </div>
  );
}
