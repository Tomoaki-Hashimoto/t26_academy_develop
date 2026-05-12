package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2
@Controller // このクラスは「画面用です」とSpringに教えるため
public class BookController {

    private final BookMstService bookMstService;// ブックコントローラーの中のブックマスタサービス型とブックマスタ変数は変更できないとした（ControllerはDB操作しないルールだから）

    @Autowired // ここでSpringが自動でServiceを入れてくれる(SpringがServiceを自動で渡してくれるため)
    public BookController(BookMstService bookMstService) {
        this.bookMstService = bookMstService;// Thisをつけることで区別する。どちらが型か変数か

    }

    @GetMapping("/book/index") // DBからデータを取得し、画面へ渡す
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();

        model.addAttribute("bookMstList", bookMstList);// HTMLに渡す準備

        return "book/index";// index.htmlを表示する
    }

    @GetMapping("/book/add") // 登録画面を表示する
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {// もし情報がなかったら
            model.addAttribute("bookMstDto", new BookMstDto());// 空のフォームを作って画面に渡している
        }
        return "book/add";// add.htmlを表示
    }

    @PostMapping("/book/add") // 保存ボタンを押したときの処理
    public String add(BookMstDto bookMstDto) {// 画面の入力データを受け取る

        this.bookMstService.save(bookMstDto);// DBに保存する処理をお願いしている

        return "redirect:/book/index";// 一覧画面に戻る
    }
}