
package me.su2in.springbootdeveloper.controller;

import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import me.su2in.springbootdeveloper.domain.Memo;
import me.su2in.springbootdeveloper.repository.MemoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemoController {

    private final MemoRepository memoRepository;
    private final DataSource dataSource;

    @GetMapping("/")
    public String index(Model model) {
        String dbName = "Unknown DB";

        try (Connection connection = dataSource.getConnection()) {
            dbName = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            dbName = "DB Connection Error (" + e.getMessage() + ")";
        }

        List<Memo> memoList = memoRepository.findAllByOrderByIdDesc();

        model.addAttribute("dbName", dbName);
        model.addAttribute("memos", memoList);
        return "index";
    }

    @PostMapping("/memo")
    public String saveMemo(@RequestParam("content") String content) {
        if (content != null && !content.trim().isEmpty()) {
            memoRepository.save(new Memo(content));
        }
        return "redirect:/";
    }
}
