export const looseUnsavedChanges = (
  e: any,
  codeSaved: boolean,
  setCodeSaved: (_: boolean) => void
): boolean => {
  if (!codeSaved) {
    const result = confirm(
      "You have unsaved changes. If you navigate away, these will not be saved."
    );
    if (!result) {
      e.preventDefault();
      return result;
    }
    setCodeSaved(true);
  }

  return true;
};
