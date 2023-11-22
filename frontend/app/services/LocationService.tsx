export async function getLocations(): Promise<string[]> {
  const list = ["Austria", "Germany", "Swiss"];
  const l = await new Promise<string[]>(function (resolve, reject) {
    resolve(list);
  });
  return l;
}
