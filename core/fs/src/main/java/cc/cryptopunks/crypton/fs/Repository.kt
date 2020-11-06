package cc.cryptopunks.crypton.fs

import cc.cryptopunks.crypton.fs.composition.CompositeParser
import cc.cryptopunks.crypton.fs.composition.CompositeRepo
import cc.cryptopunks.crypton.fs.parser.mp3.Mp3LoreParser
import cc.cryptopunks.crypton.fs.parser.yml.YmlLoreParser
import cc.cryptopunks.crypton.fs.repo.FileBlobRepo
import cc.cryptopunks.crypton.fs.repo.FileLoreLore
import cc.cryptopunks.crypton.fs.repo.TypedRepo
import java.io.File

data class Repository(
    val dir: File,
    val blobRepo: Blob.Repo = FileBlobRepo(dir),
    val loreRepo: Lore.Repo = FileLoreLore(dir),
    val parse: Lore.Parse = CompositeParser(
        Mp3LoreParser,
        YmlLoreParser
    )
) :
    Lore.Relations by loreRepo,
    TypedRepo<Data> by CompositeRepo(
        loreRepo,
        blobRepo
    )

