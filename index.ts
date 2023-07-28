import { convexHull, distance } from "./convexHull";
const input = Bun.file("points.txt");
const text = await input.text();
const points: number[][] = [];
for (const line of text.split("\n")) {
    const [lng, lat] = line.split(",").map((s) => parseFloat(s));
    points.push([lng, lat]);
}

const hull = await convexHull(points);
console.log(hull);

let sum = 0;
for (let i = 0; i < hull.length - 1; i++) {
  sum += distance(hull[i], hull[i + 1]);
}

await Bun.write("path2.txt", hull.map((p) => p.join(" ")).join("\n") + `\nSum: ${sum}`);