import pathlib
import markdown


def test_readme_can_be_parsed():
    readme = pathlib.Path(__file__).resolve().parents[1] / "README.md"
    text = readme.read_text(encoding="utf-8")
    html = markdown.markdown(text)
    assert html.strip() != ""
    assert "<h1" in html
